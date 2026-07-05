(START)		// starting conditions, set address to first pixel and reset pixel-counter to 8192
    @SCREEN
    D=A
    @addr
    M=D

    @8192	// total number of pixels to be "painted" (256 * 32)
    D=A
    @pixels
    M=D

(CHECK) 	// obtain the user input from keyboard
    @pixels	// check if there are pixels left to paint
    D=M
    @START
    D;JEQ	// if not, goto START

    @KBD	// check keyboard input at RAM[24576]
    D=M

    @BLACKSC 	// black screen if key is pressed
    D;JNE

    @BLANKSC 	//blank screen if no key is pressed
    D;JEQ

    (BLACKSC)	
	@addr	// set the pixel at addr to 1
	A=M
	M=-1

	@addr
	M=M+1

	@pixels	// one less pixel to paint
	M=M-1
	
	@CHECK	// retrieve user input
	0;JMP

    (BLANKSC)	
	@addr	// set the pixel at addr to 0
	A=M
	M=0

	@addr
	M=M+1

	@pixels
	M=M-1
	
	@CHECK
	0;JMP