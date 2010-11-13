import unittest

from brains import DataFile, NeuralNet
import numpy as np

class TestNeuralNet(unittest.TestCase):
    
    def test_train(s):
        d = np.loadtxt("data/iris.csv", skiprows = 6, delimiter = ",", comments = "#")
        np.random.shuffle(d)
        targets, inputs = d[:,0], d[:,1:]
        targets.shape = (targets.shape[0], 1)
        targets /= targets.max()
        inputs -= inputs.mean(axis = 0)
        split = d.shape[0] / 4
        testdata, traindata = inputs[:split,:], inputs[split:,:]
        order = range(traindata.shape[0])
        testtargets, traintargets = targets[:split,:], targets[split:,:]
        n = NeuralNet(np.shape(d)[1] - 1, 1, 4)
        n.train(traindata, traintargets)
        hact, p = n.process(testdata)
        for i in range(len(p)):
            continue
            print testtargets[i], p[i]
