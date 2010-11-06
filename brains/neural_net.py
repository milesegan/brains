from __future__ import division
import math
import numpy as np
from random import random

class NeuralNet(object):
    """Basic single-layer neural net implementation."""
    def __init__(s, n_inputs, n_outputs, n_hidden):
        s.eta = 0.1 # learning rate
        s.n_inputs = n_inputs
        s.n_outputs = n_outputs
        s.n_hidden = n_hidden
        s.iweights = s.__random_weights(n_hidden, n_inputs + 1) # add one for bias node
        s.oweights = s.__random_weights(n_outputs, n_hidden + 1)

    def train(s, inputs, outputs):
        hact, oact = s.process(inputs)
        # find output error
        oerr = [(outputs[i] - oact[i]) * oact[i] * (1 - oact[i]) for i in range(s.n_outputs)]
        oerr = (outputs - oact) * oact
    #     # find hidden layer error
    # // calc hidden layer error
    # val hErr = for (j <- 0 until nHidden) yield {
    #   val errProd = for (k <- 0 until nOutputs) yield oWeights(j)(k) * oErr(k)
    #   hAct(j) * (1 - hAct(j)) * errProd.sum
    # }
        print(oerr)
        for i in range(s.n_outputs):
            for j in range(s.n_hidden):
                s.oweights[i][j] += s.eta * oerr[i] * hact[j]
        # for i in range(s.n_inputs):
        #     for j in range(s.n_hidden):
        #         s.iweights[i][j] += s.eta * herr[i] * inputs[j]

    def process(s, inputs):
        """Runs the neural net on the given inputs."""
        assert len(inputs) == s.n_inputs
        hact = [s.__activation(v) for v in np.dot(s.iweights, np.append(inputs, -1))]
        oact = [s.__activation(v) for v in np.dot(s.oweights, np.append(hact, -1))]
        return hact, oact
        
    def __activation(s, val):
        """Activation function. Default to sigmoid."""
        return 1 / (1 + math.exp(val))

    def __random_weights(s, rows, columns):
        """Initializes weights to a small, non-zero value."""
        max_init = 1.0 / math.sqrt(s.n_hidden)
        w = []
        for r in range(rows):
            w.append([random() * 2 * max_init - max_init for i in range(columns)])
        return w
    
