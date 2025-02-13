--- [CREATE/CONNECT DATABASE] --------------------------------------------------
CONNECT 'jdbc:derby://localhost:1527/dbn45contacts;create=true;user=root;password=root';

--- [TRIGGERS] -----------------------------------------------------------------
DROP TRIGGER DELETE_CONTRIBUTIONS_AFTER_WISHLIST_DELETE;

DROP TRIGGER NOTIFY_WISHLIST_OWNER;

DROP TRIGGER NOTIFY_CONTRIBUTORS;

--- [VIEWS] --------------------------------------------------------------------
DROP VIEW WISHLIST_VIEW;

DROP VIEW FRIENDS_VIEW;

DROP VIEW FRIEND_REQUESTS_VIEW;

--- [MODELS] -------------------------------------------------------------------
DROP TABLE NOTIFICATION;

DROP TABLE FRIENDSHIP;

DROP TABLE CONTRIBUTION;

DROP TABLE WISHLIST;

DROP TABLE PRODUCT;

DROP TABLE MEMBER;