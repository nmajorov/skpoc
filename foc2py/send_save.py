#!/bin/env python

from pysimplesoap.client import SoapClient

client = SoapClient(
    location = "http://127.0.0.1:8008",
    action = 'http://localhost:8008/Save', # SOAPAction
    namespace = "http://skyguide.ch/web", 
    soap_ns='soap', trace = False, ns = False, exceptions=True)

response = client.save(a="skyguide")

result = response['save']
#print(result)

