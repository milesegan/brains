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
            s.pf.setdefault((feat, val), 0)
            s.pf[(feat, val)] += 1
            s.pfc.setdefault((feat, val, klass), 0)
            s.pfc[(feat, val, klass)] += 1
        s.count += 1

    def classify(s, features):
        ranked = []
        for c in s.pc.keys():
            p = 1
            for f in features:
                pfckey = f + (c,)
                if s.pfc.has_key(pfckey):
                    p *= s.pfc.get(pfckey) / s.count
                else:
                    p *= 0.01 / s.count # TODO: adjust this
            ranked.append((p * s.pc.get(c, 0.0), c))
        ranked.sort()
        return ranked[-1][1]
