#!/usr/bin/env python

sdict = {
    "author": "Miles Egan",
    "author_email": "milesegan@gmail.com",
    "name": "brains",
    "version": "0.1",
    "test_suite" : "tests",
    "url": "http://github.com/cageface/brains",
    "packages": ['brains']
}

try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup
    
setup(**sdict)
