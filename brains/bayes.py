# simple bayesian classifier in python

from __future__ import division
import random
import os, re, sys

class Bayes(object):

    def __init__(s):
        s.pc = {}
        s.pf = {}
        s.pfc = {}
        s.count = 0.0

    def train(s, klass, features):
        s.pc.setdefault(klass, 0)
        s.pc[klass] += 1
        for feat, val in features:
            s.pf.setdefault(feat, dict())
            s.pf[feat].setdefault(val, 0)
            s.pf[feat][val] += 1
            s.pfc.setdefault(feat, dict())
            s.pfc[feat].setdefault(val, dict())
            s.pfc[feat][val].setdefault(klass, 0)
            s.pfc[feat][val][klass] += 1
        s.count += 1

    def classify(s, features):
        ranked = []
        for klass in s.pc.iterkeys():
            p = 1
            for feat, val in features:
                c = s.pfc.get(feat, {}).get(val, {}).get(klass, None)
                if c:
                    p *= c / s.count
                else:
                    p *= 0.01 / s.count # TODO: adjust this
            ranked.append((p * s.pc.get(klass, 0.0), klass))
        ranked.sort()
        return ranked[-1][1]
