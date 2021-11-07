FROM tomcat:9.0
ADD target/whatIsThisBird-1.0.war /usr/local/tomcat/webapps/ROOT.war
ADD resources/web.xml /usr/local/tomcat/webapps/manager/WEB-INF
COPY --from=redis:6.0.1 /usr/local/bin/redis-server /usr/local/bin/redis-server
ADD resources/redis.conf /usr/local/etc/
ADD redisDb/dump.rdb /usr/local/var/db/redis/
COPY resources/start.sh start.sh
RUN chmod +x *.sh
EXPOSE 8080
CMD ["./start.sh"]
