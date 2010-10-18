require 'matrix'

module Brains

# Standard MLP with a single hidden layer and a 
# sigmoidal activation function.
class NeuralNet

  attr_reader :inputs, :nodes, :outputs, :hweights, :oweights

  # [inputs] number of input nodes
  # [nodes] number of hidden layer nodes
  # [outputs] number of output nodes
  def initialize(inputs, nodes, outputs)
    @eta = 0.1
    @beta = 1.0
    @inputs = inputs
    @nodes = nodes
    @outputs = outputs

    # add one to array sizes for bias node
    @hweights = Matrix.of_dims(inputs + 1, nodes) { random_weight }
    @oweights = Matrix.of_dims(nodes + 1, outputs) { random_weight }
  end

  # Classifies the input with weights learned during training.
  #
  # returns The output of the network for the given input.
  def classify(input)
  end

  # Trains the network, updating weights.
  #
  # [input] The array of numeric values going in to the network.
  # [output] The correct output for the given input.
  def train(input, output)
  end

  private
  def random_weight
    max = 1.0 / Math.sqrt(@nodes)
    rand * 2 * max - max
  end

  def activation(v)
    1.0 / (1.0 + Math.exp(-@beta * v)) # sigmoid activation
  end

  def error(a, b)
    fail if a.size != b.size
    a.zip(b).collect { |a,b| Math.pow(a - b, 2) }.sum
  end

end

end

