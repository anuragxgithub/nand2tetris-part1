@R0
D=M
@m
M=D   // m =

@R1
D=M
@n
M=D   // n = 

@i
M=D

@sum
M=0

(LOOP)
    // stopping condition
    @i
    D=M
    @STOP
    D;JEQ   // Jump to stop if D(i) = 0


    @sum
    D=M
    @m
    D=D+M
    @sum
    M=D

    @i
    M=M-1
	
    @LOOP
    0;JMP

(STOP)
    @sum
    D=M
    @R2
    M=D
    @STOP
    0;JMP





