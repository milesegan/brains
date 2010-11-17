import unittest

import brains
import numpy as np

class PcaTestCase(unittest.TestCase):

    def test_pca(s):
        d = np.loadtxt("data/housing.csv", delimiter=",")
        p = brains.pca(d)
        assert p.shape[1] == 2
        
