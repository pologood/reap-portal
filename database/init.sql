-- 参数
INSERT INTO CONFIG (ID,APPLICATION,PROFILE,LABEL,NAME,VALUE) VALUES ('reap-portal.prd.default.server.port','reap-portal','prd','default','server.port','8081');
INSERT INTO CONFIG (ID,APPLICATION,PROFILE,LABEL,NAME,VALUE) VALUES ('reap-portal.prd.default.token.key','reap-portal','prd','default','token.key','123456');

-- 路由规则
INSERT INTO ROUTE VALUES('reap-portal','reap-portal','/apis/reap-portal/**','reap-portal',null,null);
INSERT INTO ROUTE VALUES('reap-portal-ui','reap-portal-ui','/ui/reap-portal/**','reap-portal',null,null);

-- 功能码
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPPO0001','ouip-portal','REAPPO0001','菜单管理','M','');
