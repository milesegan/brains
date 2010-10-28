class DataFile

  FEATURES = "one two three four five six".split

  def self.make_real(size = 100)
    d = DataFile.new
    d.instance_eval do
      @names = FEATURES
      @size = size
      @samples = []
      size.times do
        @samples << Array.new(FEATURES.size) { rand }
      end
    end
    d
  end

  def self.make_categorical
  end

end
