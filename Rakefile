$: << File.join(File.dirname(__FILE__), "lib")

task :default do
  # just run tests, nothing fancy
  Dir["test/*.rb"].sort.each { |test| load test }
end
