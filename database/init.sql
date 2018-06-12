-- 参数
INSERT INTO CONFIG (ID, APPLICATION, PROFILE, LABEL, NAME, VALUE) VALUES ('r-p.p.d.s.port', 'reap-portal', 'prd', 'default', 'server.port', '8081');
INSERT INTO CONFIG (ID, APPLICATION, PROFILE, LABEL, NAME, VALUE) VALUES ('r-p.p.d.t.key', 'reap-portal', 'prd', 'default', 'token.key', '123456');
INSERT INTO CONFIG (ID, APPLICATION, PROFILE, LABEL, NAME, VALUE) VALUES ('r-p.p.d.t.s.timeout', 'reap-portal', 'prd', 'default', 'session.timeout', '1200000');

-- 路由规则
INSERT INTO ROUTE VALUES('reap-portal', 'reap-portal' ,'/apis/reap-portal/**' ,'reap-portal' ,null ,null);
INSERT INTO ROUTE VALUES('reap-portal-ui', 'reap-portal-ui', '/ui/reap-portal/**' ,'reap-portal' ,null ,null);

-- 功能码
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPPO0001','reap-portal','REAPPO0001','菜单管理','M','');

-- 菜单
INSERT INTO MENU (ID, NAME, PARENT_ID, LEVEL,SEQUENCE, LEAF, FUNCTION_CODE,REMARK,CREATE_TIME) VALUES ('0001' , '菜单管理', NULL, 1, 1,'Y','REAPPO0001', NULL ,NULL);
