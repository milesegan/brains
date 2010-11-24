import unittest

from brains import DataFile, NeuralNet, pca
import numpy as np

class TestNeuralNet(unittest.TestCase):
    
    def test_train(s):
        d = np.loadtxt("data/iris.csv", skiprows = 6, delimiter = ",", comments = "#")
        np.random.shuffle(d)
        d /= np.abs(d).max(axis=0)
        targets, inputs = d[:,0], d[:,1:]
        # do pca first to minimize input noise
        inputs = pca(inputs)
        targets.shape = (targets.shape[0], 1)
        inputs -= inputs.mean(axis = 0)
        split = d.shape[0] / 5
        testdata, traindata = inputs[:split,:], inputs[split:,:]
        testtargets, traintargets = targets[:split,:], targets[split:,:]
        n = NeuralNet(np.shape(inputs)[1], 1, 6)
        n.train(traindata, traintargets)
        hact, p = n.process(testdata)
        for i in range(len(p)):
            print testtargets[i], p[i]
