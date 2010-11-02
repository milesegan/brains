import unittest

import brains

class ProbabilityMapTestCase(unittest.TestCase):

    def test_create(s):
        p = brains.ProbabilityMap()
        p.update("foo", {"fob": "1", "foc": "2", "fod": "3"})
        p.update("foo", {"fob": "4", "foc": "5", "fod": "6"})
        p.update("bar", {"bar": "1", "bar": "2", "bar": "3"})
        s.assertEquals(p.count, 3)
        s.assertEquals(p.most_common_class, "foo")
        s.assertEquals(p.classes, ["bar", "foo"])
        s.assertEquals(p.features, ["bar", "fob", "foc", "fod"])
        s.assertEquals(p.feature_values("fob"), ["1", "4"])

