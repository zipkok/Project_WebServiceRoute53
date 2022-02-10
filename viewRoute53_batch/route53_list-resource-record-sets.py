#!/usr/bin/env python3.10
import boto3
import pymysql
import json

# aws route53 list_resource_record_sets --hosted-zone-id <hosted_zone_ID>
def list_resource_record_sets():
    route53 = boto3.client('route53')
    response = route53.list_resource_record_sets(HostedZoneId='Z0338876FY905DAOM13I')
    
    truncate_mysql()
    for ResourceRecordsCounts in response['ResourceRecordSets']:
        res = [ ResourceRecordsCounts['Name'],  ResourceRecordsCounts['Type'], ResourceRecordsCounts['ResourceRecords'], ResourceRecordsCounts['TTL'] ]
        connect_mysql(res)
    
# aws route53 list-hosted-zones --profile 
def list_hosted_zones():
    route53 = boto3.client('route53')

def truncate_mysql(*args):
    # DB Connection Data
    host = "localhost"
    username = "root"
    password = "fdsa1234"
    port = 3306
    database = "aws_db"
    
    # Insert Query
    conn = pymysql.connect(host=host, user=username, passwd=password, port=port, database=database)
    truncate_query = "truncate table list_resource_record_sets"
    
    cursors = conn.cursor()
    cursors.execute(truncate_query)
    
def connect_mysql(*args):
    # Argument Parsing
    data = ''
    for i in range(len(args[0][2])):
        data += args[0][2][i]['Value']
    
    # DB Connection Data
    host = "localhost"
    username = "root"
    password = "fdsa1234"
    port = 3306
    database = "aws_db"
    
    # Insert Query
    conn = pymysql.connect(host=host, user=username, passwd=password, port=port, database=database)
    
    
    query = "INSERT INTO list_resource_record_sets \
        (record_name, record_type, record_routing_table, record_value, record_ttl, record_hosted_id) VALUE \
        (%s, %s, 'routing_table', %s, %s , 'woobeom')"
        
    cursors = conn.cursor()
    cursors.execute( query, (args[0][0], args[0][1], data, args[0][3] ))    
     
    
    # Query Commit
    conn.commit()
    conn.close()

    
if __name__ == "__main__":
    list_resource_record_sets()
    # testCode()
    