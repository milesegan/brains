#!/usr/bin/env python

sdict = {
    'name': "brains",
    'version': "0.1",
    'test_suite' : "tests"
}

try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup
    
setup(**sdict)
