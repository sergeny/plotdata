import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:mysql://localhost:8890/test", "grails", "grails", "com.mysql.jdbc.Driver")