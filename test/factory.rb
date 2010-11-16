$: << File.join(File.dirname(__FILE__), "..", "lib")

require "brains"

class Brains::DataFile

  FEATURES = "one two three four five six".split

  def self.make_real(size = 100)
    # generate bogus real-valued test data
    # just duck type this out as a hash instead
    # of a datafile instance
    features = "one two three four five six".split.collect(&:to_sym)
    samples = []
    size.times do
      h = {}
      features.each do |f|
        h[f] = rand
      end
      samples << h
    end
    samples
  end

  def self.make_categorical
  end

end
