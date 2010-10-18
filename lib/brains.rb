require 'brains/neural_net'
require 'matrix'

class Matrix

  # Add missing direct assignment method.
  def []=(i, j, v)
    @rows[i][j] = v
  end

  # Handy create + initialize function.
  def self.of_dims(m, n, init = 0.0, &block)
    elems = Array.new(m) { Array.new(n, init) }
    mat = Matrix[*elems]
    if block_given?
      m.times do |i|
        n.times do |j|
          mat[i,j] = yield(i, j)
        end
      end
    end
    mat
  end

end
