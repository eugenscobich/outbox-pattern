CREATE USER ${springDatasourceUsername} WITH ENCRYPTED PASSWORD '${springDatasourcePassword}';

GRANT USAGE ON SCHEMA ${springJpaPropertiesHibernateDefaultSchema} TO ${springDatasourceUsername};

GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA ${springJpaPropertiesHibernateDefaultSchema} TO ${springDatasourceUsername};