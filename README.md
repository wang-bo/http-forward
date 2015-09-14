# http-forward
一个简单的http转发器

使用说明：
	1. HTTP请求转发器，可以把外网的请求转发到内网的服务上。
	2. 目前只实现了最基本的功能，加载资源较多的网页时性能较差。
	3. 目前的实现比较适合转发响应为文本数据的单个http请求，如第三方服务的回调，可以转发到本地开发环境，方便调试。

实现思路：
	1. 转发分为服务端和客户，下面分别简称为server和client；
	2. server部署在服务器上，client部署在客户机上(可以是内网的机器)；
	2. client需配置一个clientNo(客户端编号)，并与server保持长连接(目前用http模拟长连接)；
	3. server需配置一组clientNo，client请求保持长连接时，验证client的clientNo是否合法；
	4. server所在服务器上配置nginx，把clientNo.serverdomain.com/path的请求转发到server上，示例：
			server {
				listen       80;
				server_name  *.serverdomain.com;
				if ($host ~* "^(.*)\.serverdomain\.com$") {
					set $id $1;
					rewrite ^(.*)$      /transpondRequest/$id$1     last;
				}			
				location / {
					proxy_pass   http://127.0.0.1:8880;
				}        
			}
	5. client需配置target_server_url(可以是内网的服务)；
	6. 当有客户端(如浏览器)请求clientNo.serverdomain.com/path时，server会把请求数据封装，转发给client；
	7. client接收到server转发过来的请求时，把请求转发到target_server_url，再把target_server_url的response数据封装，转发给server；
	8. server收到client转发过来的response时，响应客户端。
	
没处理的问题：
	1. client长连接server时，只是简单判断了clientNo，没有做其他更强的认证机制；
	2. 使用http协议模拟长连接，性能不高，不过穿透防火墙容易；
	3. client转发response时，没有按Content-Type对body进行相应处理，而是统一把body做base64编码，server端做base64解码，效率不高；
	4. 各待处理任务没有使用任务队列，而是简单的开启一个线程进行处理，导致容易丢失数据，线程也容易失控；
	5. 没有考虑对转发大文件的支持，转发大文件时估计性能很差，容易失败；
	6. 没有根据目标服务器返回的编码处理，会乱码；
	7. 没有根据目标服务器返回的缓存策略使用缓存，加载页面时所有的资源请求都要转发，容易加载失败；
	8. 没有统一处理异常。
