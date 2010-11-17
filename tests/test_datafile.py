import unittest

import brains

class DataFileTestCase(unittest.TestCase):

    def test_load(s):
        f = brains.DataFile("data/mushroom.csv")
        allp = [p for p in f.points()]
        assert f.count == 8124
        assert f.features[0] == "cap-shape"
        assert allp[0][0] == "p"
        assert allp[0][1]["cap-shape"] == "x"
