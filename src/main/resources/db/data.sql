INSERT INTO member (
    username,
    password,
    role,
    description,
    last_login_at,
    login_fail_count
) VALUES
      ('SYSTEM_ADMIN',   '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'SYSTEM_ADMIN',   NULL, NULL, 0),
      ('SYSTEM_MANAGER', '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'SYSTEM_MANAGER', NULL, NULL, 0),
      ('CONTENT_MANAGER','$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'CONTENT_MANAGER',NULL, NULL, 0),
      ('CONTENT_ADMIN',  '$2a$10$J5XR/L4Nb6DFw5HopHoXHeED.PMiyoeXLeqzKg8ad3om8YffcuB4a', 'CONTENT_ADMIN',  NULL, NULL, 0);





INSERT INTO MENU (name, description, url, icon, roles, is_active,parent_id)
    values
        -- 부모메뉴
    ('DashBoard', 'DashBoard','/','mdi-view-dashboard','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null),
    ('학습관리', '학습관리','','mdi-clipboard','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null),
    ('모델관리', '모델관리','','mdi-lan-connect','CONTENT_MANAGER,CONTENT_ADMIN,SYSTEM_MANAGER,SYSTEM_ADMIN',true, null),
    ('시스템 설정', '시스템 설정','','mdi-cog','CONTENT_ADMIN,SYSTEM_ADMIN',true, null),
    ('통계조회', '통계조회','','mdi-database-clock','CONTENT_ADMIN,SYSTEM_ADMIN',true, null),

    -- 학습관리 하위 메뉴
    ('학습문서', '학습문서','/learning-management/document-ingestion','mdi-clipboard-edit','SYSTEM_ADMIN',true,2),
    ('학습 데이터 생성', '학습 데이터 생성','/learning-management/data-generation','mdi-book-plus','SYSTEM_ADMIN',true,2),
    ('벡터데이터 관리', '벡터데이터 관리','/learning-management/vector-management','mdi-database-edit-outline','SYSTEM_ADMIN',true,2),

    -- 시스템 설정
    ('메뉴/권한관리', '메뉴/권한관리','/system/menu-management','mdi-menu-open','SYSTEM_ADMIN',true,4),
    ('사용자 관리', '사용자 관리','/system/member-management','mdi-account-edit','SYSTEM_ADMIN',true,4),
    ('공통코드 관리', '공통코드 관리','/system/commoncode-management','mdi-file-code','SYSTEM_ADMIN',true,4),
    ('임계치 관리', '임계치관리','/system/threshold-management','mdi-speedometer','SYSTEM_ADMIN',true, 4),
    ('도움말 관리', '도움말 관리','/system/help-dialog','mdi-help-circle','SYSTEM_ADMIN',true, 4),

    -- 통계 관리
    ('이용률 통계', '메뉴/권한관리','/statistics/utilization-rate','mdi-database-search-outline','SYSTEM_ADMIN',true,5),
    ('키워드 통계', '사용자 관리','/statistics/keyword-statistics','mdi-database-search-outline','SYSTEM_ADMIN',true,5),
    ('답변정확도 통계', '공통코드 관리','/statistics/answer-accuracy-statistics','mdi-database-search-outline','SYSTEM_ADMIN',true,5);

