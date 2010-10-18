import unittest

import brains

class BayesTestCase(unittest.TestCase):

    def test_create(s):
        b = brains.Bayes()
        b.train("foo", [["foo", "1"], ["foo", "2"], ["foo", "3"]])
        b.train("bar", [["bar", "1"], ["bar", "2"], ["bar", "3"]])
        s.assertEquals(b.count, 2)

    def test_load(s):
        b = brains.Bayes()
        f = brains.DataFile("data/mushroom.csv")
        for p in f.points():
            b.train(p[0], p[1])
        s.assertEquals(b.count, 8124)
        s.assertEquals(b.pc['p'], 3916)
