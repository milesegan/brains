import unittest

from brains import DataFile, NeuralNet
import numpy as np

class TestNeuralNet(unittest.TestCase):
    
    def test_create(s):
        n = NeuralNet(3, 1, 4)
        s.assertEqual(np.shape(n.iweights), (4, 4))
        s.assertEqual(np.shape(n.oweights), (1, 5))
        
    def test_train(s):
        d = np.loadtxt("data/wine.csv", skiprows = 2, delimiter = ",")
        n = NeuralNet(np.shape(d)[1] - 1, 1, 4)
        for i in range(10):
            for p in d:
                n.train(p[1:], [p[0]])
