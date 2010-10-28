# datafile loading library

class DataFile

  attr_reader :size, :names, :samples
  SEP = /\s*,\s*/

  def self.load(path)
    file = open(path)
    while line = file.readline
      break unless line =~ /^\s*#/ # skip comments
    end
    klass, *names = line.strip.split(SEP)
    names.collect! { |i| i.to_sym }
    d = DataFile.new
    d.instance_eval do
      @file = file
      @size = 0
      @samples = nil
      @names = names
    end
    d
  end

  def each_sample
    @file.each_line do |f|
      @size += 1
      klass, *parts = f.strip.split(SEP)
      next unless parts.size == @names.size
      yield [klass, parts]
    end
  end

  def read!
    if @samples.nil?
      @samples = []
      each_sample { |k,f| @samples << [k, f] }
    end
  end

  def shuffle!
    read!
    @samples = @samples.shuffle
  end

  def split(fraction)
    read!
    index = @samples.size / fraction
    [@samples[0, index], @samples[index, @samples.size - 1]]
  end

end
