#!/usr/bin/env python
from __future__ import absolute_import, division, print_function, with_statement
from pysimplesoap.server import SoapDispatcher, SOAPHandler
from BaseHTTPServer import HTTPServer



#
# sample implementation of FOC servers
# author: Nikolaj Majorov nmajorov@redhat.com
#


port = 8008


def save(a):
    print("called save with parameter {0}".format(a)) 
    return "{0} saved".format(a)

dispatcher = SoapDispatcher(
    'my_dispatcher',
    location = "http://localhost:{0}/".format(port),
    action = 'http://localhost:{0}/'.format(port), # SOAPAction
    namespace = "http://skyguide.ch/web", prefix="sky",
    trace = True,
    ns = True)

# register the user function
dispatcher.register_function('Save', save,
    returns={'SaveResult': str}, 
    args={'a': str})

print("Starting foc server on port {0} ...".format(port))
httpd = HTTPServer(("", port), SOAPHandler)
httpd.dispatcher = dispatcher
httpd.serve_forever()
