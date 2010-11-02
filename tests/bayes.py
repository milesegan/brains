import unittest

import brains

class BayesTestCase(unittest.TestCase):

    def test_create(s):
        b = brains.Bayes()
        b.train("foo", [["foo", "1"], ["foo", "2"], ["foo", "3"]])
        b.train("bar", [["bar", "1"], ["bar", "2"], ["bar", "3"]])
        s.assertEquals(b.count, 2)

    def test_classify(s):
        b = brains.Bayes()
        f = brains.DataFile("data/mushroom.csv")
        test, train = f.split(4)
        for p in train:
            b.train(p[0], p[1])
        correct = 0.0
        for t in test:
            predict = b.classify(t[1])
            if t[0] == predict:
                correct += 1
        s.assertGreater(correct / len(test), 0.9)



        
