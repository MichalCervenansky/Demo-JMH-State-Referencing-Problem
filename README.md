# Demo JMH State Referencing Problem
I have come across a usecase where I need to reference class with @State Scope.Benchmark in class with @State Scope.Thread. This is possible in JMH. However, when tried to use it twice as seen in StateReferencingNotWorking it was not able to build(mvn clean install) due to:    


/home/Example-jmh-state-referencing/target/classes/demo/StateReferencing/generated/StateReferencingNotWorking_sampleTime_jmhTest.java:[551,207] variable l_statereferencingnotworking0_G is already defined in method _jmh_tryInit_f_threadscope1_0(org.openjdk.jmh.runner.InfraControl,demo.StateReferencing.generated.StateReferencingNotWorking_jmhType,demo.StateReferencing.generated.StateReferencingNotWorking_jmhType)    


It seems that two parameters of the same type are generated into variables of the same names which causes this error. The Workaround can be seen in StateReferencing class, but it is less intuitive in my opinion.
