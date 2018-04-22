import java_cup.runtime.Symbol;


class Lexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

	StringBuffer string = new StringBuffer();
	int indentCount = 0;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Lexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Lexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Lexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		75
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_START,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NOT_ACCEPT,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"56:10,3,56:2,2,56:18,6,39,1,4,56,34,40,54,47,5,33,34,51,34,44,35,43:10,49,4" +
"8,37,36,38,56,50,42:5,32,42:7,20,42:5,31,42:6,52,55,53,40,42,56,10,23,8,24," +
"17,12,27,26,13,42,29,9,22,14,21,28,42,16,11,18,19,42,25,30,15,42,45,40,46,4" +
"1,56,7,0")[0];

	private int yy_rmap[] = unpackFromString(1,148,
"0,1,2,1:2,3,4,5,6,7,6,8,9,1:10,10,11,12:3,13,1,14,15,1:5,16,13,1,17,18,8,1," +
"19,1:2,20,1:2,21,22,23,24,1,20,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39" +
",40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64" +
",65,66,67,68,69,70,71,72,73,74,75,76,77,78,12,79,80,81,82,83,12,84,85,86,87" +
",88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,12" +
",109,110,111,112,113")[0];

	private int yy_nxt[][] = unpackFromString(114,57,
"1,2,-1,3,40,4,5,38,6,139,41,142,85,52,87,143,144,109,88,142,111,56,142,145," +
"89,112,142,146,113,142:2,114,147,7,42,53,8,9,44,10,11,46,142,12,13,14,15,16" +
",17,18,19,20,21,22,39,45:2,-1:58,37,-1:61,5,-1:58,142,113,142:11,115,142:11" +
",-1:9,142,116,-1:46,42,-1:2,43,-1:56,54,-1:56,54,11,54,-1:54,43,-1:63,12,55" +
",-1:20,142:3,127,142:21,-1:9,142,116,-1:19,59,-1,142:25,-1:9,142,116,-1:21," +
"142:25,-1:9,142,116,-1:19,51,-1:54,30:3,-1,30:46,-1:2,30,-1,32,-1:12,33,-1," +
"34,-1,35,-1:35,36,-1:3,57,-1:56,40:2,-1,40:3,-1,40:49,-1:8,142:3,23,142:2,5" +
"8,142:18,-1:9,142,116,-1:49,54,-1,11,-1:61,47,-1:19,67,-1,142:25,-1:9,142,1" +
"16,-1:19,86,-1:58,142:3,24,110,142,25,142:7,118,142:10,-1:9,142,116,-1:48,4" +
"2,43,-1:28,142:8,26,142:16,-1:9,142,116,-1:14,61,57:3,-1,57,-1,57:39,-1,57:" +
"9,-1:8,142:16,26,142:8,-1:9,142,116,-1:27,65,-1:50,142:8,110,142:16,-1:9,14" +
"2,116,-1:14,69,-1:63,142:6,130,142:3,50,142:14,-1:9,142,116,-1:19,28,-1:58," +
"142:7,110,142:17,-1:9,142,116,-1:34,71,-1:43,142,110,142:2,110,142:20,-1:9," +
"142,116,-1:26,73,-1:51,142:14,110,142:10,-1:9,142,116,-1:14,3,-1:63,142:9,1" +
"10,142:15,-1:9,142,116,-1:31,48,-1:46,142:4,110,142:20,-1:9,142,116,-1:27,4" +
"9,-1:50,110,142:24,-1:9,142,116,-1:13,1,29,-1:2,30:3,1,30:46,29,31,30,-1:8," +
"142:18,110,142:6,-1:9,142,116,-1:21,142:3,110,142:21,-1:9,142,116,-1:21,142" +
":9,27,142:15,-1:9,142,116,-1:21,142:16,110,142:8,-1:9,142,116,-1:21,142:21," +
"110,142:3,-1:9,142,116,-1:21,142:2,110,142:22,-1:9,142,116,-1:21,142:10,110" +
",142:14,-1:9,142,116,-1:21,142:6,110,142:18,-1:9,142,116,-1:21,142,110,142:" +
"23,-1:9,142,116,-1:21,142:5,140,142:2,90,142:4,60,142:11,-1:9,142,116,-1:19" +
",63,-1:58,142:13,62,142:11,-1:9,142,116,-1:21,142:8,64,142:16,-1:9,142,116," +
"-1:21,142:9,66,142:15,-1:9,142,116,-1:21,142:13,68,142:11,-1:9,142,116,-1:2" +
"1,142:3,70,142,72,142:19,-1:9,142,116,-1:21,132,142:8,74,142:15,-1:9,142,11" +
"6,-1:21,142:6,70,142:18,-1:9,142,116,-1:21,142:10,76,142:14,-1:9,142,116,-1" +
":21,142:3,77,142:21,-1:9,142,116,-1:21,142:11,78,142:13,-1:9,142,116,-1:21," +
"142,79,142:23,-1:9,142,116,-1:21,142:3,70,142:21,-1:9,142,116,-1:21,142:2,8" +
"0,142:22,-1:9,142,116,-1:21,142,70,142:23,-1:9,142,116,-1:21,142:3,78,142:2" +
"1,-1:9,142,116,-1:21,142:16,81,142:8,-1:9,142,116,-1:21,142:8,82,142:16,-1:" +
"9,142,116,-1:21,142:8,83,142:16,-1:9,142,116,-1:21,142:20,82,142:4,-1:9,142" +
",116,-1:21,142:2,84,142:22,-1:9,142,116,-1:21,142,64,142:23,-1:9,142,116,-1" +
":21,142:11,70,142:13,-1:9,142,116,-1:21,142,91,142:20,92,142:2,-1:9,142,116" +
",-1:21,142:13,93,142:11,-1:9,142,116,-1:21,142:5,94,142:12,123,142:6,-1:9,1" +
"42,116,-1:21,142:2,95,142:22,-1:9,142,116,-1:21,142:8,96,142:16,-1:9,142,11" +
"6,-1:21,142:6,141,142:18,-1:9,142,116,-1:21,142:14,126,142:10,-1:9,142,116," +
"-1:21,142:20,129,142:4,-1:9,142,116,-1:21,142:9,97,142:15,-1:9,142,116,-1:2" +
"1,142:5,98,142:19,-1:9,142,116,-1:21,142:10,131,142:14,-1:9,142,116,-1:21,1" +
"42:9,99,142:15,-1:9,142,116,-1:21,142:5,100,142:19,-1:9,142,116,-1:21,142:1" +
"3,133,142:11,-1:9,142,116,-1:21,142,101,142:23,-1:9,142,116,-1:21,142:15,10" +
"2,142:9,-1:9,142,116,-1:21,142:9,103,142:15,-1:9,142,116,-1:21,142:2,135,14" +
"2:22,-1:9,142,116,-1:21,142:13,103,142:11,-1:9,142,116,-1:21,142,136,142:23" +
",-1:9,142,116,-1:21,142:11,104,142:13,-1:9,142,116,-1:21,142:9,105,142:15,-" +
"1:9,142,116,-1:21,142:15,106,142:9,-1:9,142,116,-1:21,142:5,137,142:19,-1:9" +
",142,116,-1:21,142,107,142:23,-1:9,142,116,-1:21,142:13,138,142:11,-1:9,142" +
",116,-1:21,142:6,108,142:18,-1:9,142,116,-1:21,106,142:24,-1:9,142,116,-1:2" +
"1,142:2,117,142:22,-1:9,142,116,-1:21,142:6,128,142:18,-1:9,142,116,-1:21,1" +
"42:10,134,142:14,-1:9,142,116,-1:21,142:5,119,142:19,-1:9,142,116,-1:21,142" +
":2,120,142:6,121,142:15,-1:9,142,116,-1:21,142:8,122,142:16,-1:9,142,116,-1" +
":21,142,124,142:23,-1:9,142,116,-1:21,142:2,125,142:22,-1:9,142,116,-1:13");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return new Symbol(sym.EOF, null);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
  string.setLength(0); yybegin(STRING);
}
					case -3:
						break;
					case 3:
						{
}
					case -4:
						break;
					case 4:
						{
  return new Symbol(sym.RB, null);
}
					case -5:
						break;
					case 5:
						{ }
					case -6:
						break;
					case 6:
						{
  return new Symbol(sym.ID, yytext());
}
					case -7:
						break;
					case 7:
						{
  return new Symbol(sym.ARTH_OP,yytext());
}
					case -8:
						break;
					case 8:
						{
  return new Symbol(sym.ASSIGN_OP, yytext());
}
					case -9:
						break;
					case 9:
						{
  return new Symbol(sym.COMP_OP, yytext());
}
					case -10:
						break;
					case 10:
						{
  return new Symbol(sym.error, yytext());
}
					case -11:
						break;
					case 11:
						{
  return new Symbol(sym.BITWISE_OP, yytext());
}
					case -12:
						break;
					case 12:
						{
  return new Symbol(sym.NUM, yytext());
}
					case -13:
						break;
					case 13:
						{
  return new Symbol(sym.DOT, null);
}
					case -14:
						break;
					case 14:
						{
  return new Symbol(sym.LC, null);
}
					case -15:
						break;
					case 15:
						{
  return new Symbol(sym.RC, null);
}
					case -16:
						break;
					case 16:
						{
  return new Symbol(sym.LB, null);
}
					case -17:
						break;
					case 17:
						{
  return new Symbol(sym.SMCOL, null);
}
					case -18:
						break;
					case 18:
						{
  return new Symbol(sym.COL, null);
}
					case -19:
						break;
					case 19:
						{
  return new Symbol(sym.AT, null);
}
					case -20:
						break;
					case 20:
						{
  return new Symbol(sym.COMMA, null);
}
					case -21:
						break;
					case 21:
						{
  return new Symbol(sym.LS, null);
}
					case -22:
						break;
					case 22:
						{
  return new Symbol(sym.RS, null);
}
					case -23:
						break;
					case 23:
						{
  int lex = sym.KW;
  switch (yytext()) {
    case "if": lex = sym.IF; break;
    case "else": lex = sym.ELSE; break;
    case "elif": lex = sym.ELIF; break;
    case "for": lex = sym.FOR; break;
    case "while": lex = sym.WHILE; break;
    case "from": lex = sym.FROM; break;
    case "import": lex = sym.IMPORT; break;
    case "def": lex = sym.DEF; break;
    case "try": lex = sym.TRY; break;
    case "except": lex = sym.EXCEPT; break;
    case "continue": lex = sym.CONTINUE; break;
    case "break": lex = sym.BREAK; break;
  }
  return new Symbol(lex, null);
}
					case -24:
						break;
					case 24:
						{
  return new Symbol(sym.ID_OP, yytext());
}
					case -25:
						break;
					case 25:
						{
  return new Symbol(sym.MEM_OP, yytext());
}
					case -26:
						break;
					case 26:
						{
  return new Symbol(sym.LOGICAL_OP, yytext());
}
					case -27:
						break;
					case 27:
						{
  return new Symbol(sym.BOOL,yytext());
}
					case -28:
						break;
					case 28:
						{
	int currCount = yytext().length()/4;
	indentCount = currCount;
 	return new Symbol(sym.INDENT, currCount+"");
}
					case -29:
						break;
					case 29:
						{
	yybegin(YYINITIAL);
	return new Symbol(sym.STR, string.toString());
}
					case -30:
						break;
					case 30:
						{
	string.append(yytext());
}
					case -31:
						break;
					case 31:
						{
	string.append('\\');
}
					case -32:
						break;
					case 32:
						{
	string.append('\"');
}
					case -33:
						break;
					case 33:
						{
	string.append('\n');
}
					case -34:
						break;
					case 34:
						{
	string.append('\r');
}
					case -35:
						break;
					case 35:
						{
	string.append('\t');
}
					case -36:
						break;
					case 36:
						{
	string.append('\'');
}
					case -37:
						break;
					case 38:
						
					case -38:
						break;
					case 39:
						{
  string.setLength(0); yybegin(STRING);
}
					case -39:
						break;
					case 40:
						{
}
					case -40:
						break;
					case 41:
						{
  return new Symbol(sym.ID, yytext());
}
					case -41:
						break;
					case 42:
						{
  return new Symbol(sym.ARTH_OP,yytext());
}
					case -42:
						break;
					case 43:
						{
  return new Symbol(sym.ASSIGN_OP, yytext());
}
					case -43:
						break;
					case 44:
						{
  return new Symbol(sym.COMP_OP, yytext());
}
					case -44:
						break;
					case 45:
						{
  return new Symbol(sym.error, yytext());
}
					case -45:
						break;
					case 46:
						{
  return new Symbol(sym.BITWISE_OP, yytext());
}
					case -46:
						break;
					case 47:
						{
  return new Symbol(sym.NUM, yytext());
}
					case -47:
						break;
					case 48:
						{
  return new Symbol(sym.ID_OP, yytext());
}
					case -48:
						break;
					case 49:
						{
  return new Symbol(sym.MEM_OP, yytext());
}
					case -49:
						break;
					case 50:
						{
  return new Symbol(sym.LOGICAL_OP, yytext());
}
					case -50:
						break;
					case 52:
						{
  return new Symbol(sym.ID, yytext());
}
					case -51:
						break;
					case 53:
						{
  return new Symbol(sym.ARTH_OP,yytext());
}
					case -52:
						break;
					case 54:
						{
  return new Symbol(sym.COMP_OP, yytext());
}
					case -53:
						break;
					case 56:
						{
  return new Symbol(sym.ID, yytext());
}
					case -54:
						break;
					case 58:
						{
  return new Symbol(sym.ID, yytext());
}
					case -55:
						break;
					case 60:
						{
  return new Symbol(sym.ID, yytext());
}
					case -56:
						break;
					case 62:
						{
  return new Symbol(sym.ID, yytext());
}
					case -57:
						break;
					case 64:
						{
  return new Symbol(sym.ID, yytext());
}
					case -58:
						break;
					case 66:
						{
  return new Symbol(sym.ID, yytext());
}
					case -59:
						break;
					case 68:
						{
  return new Symbol(sym.ID, yytext());
}
					case -60:
						break;
					case 70:
						{
  return new Symbol(sym.ID, yytext());
}
					case -61:
						break;
					case 72:
						{
  return new Symbol(sym.ID, yytext());
}
					case -62:
						break;
					case 74:
						{
  return new Symbol(sym.ID, yytext());
}
					case -63:
						break;
					case 76:
						{
  return new Symbol(sym.ID, yytext());
}
					case -64:
						break;
					case 77:
						{
  return new Symbol(sym.ID, yytext());
}
					case -65:
						break;
					case 78:
						{
  return new Symbol(sym.ID, yytext());
}
					case -66:
						break;
					case 79:
						{
  return new Symbol(sym.ID, yytext());
}
					case -67:
						break;
					case 80:
						{
  return new Symbol(sym.ID, yytext());
}
					case -68:
						break;
					case 81:
						{
  return new Symbol(sym.ID, yytext());
}
					case -69:
						break;
					case 82:
						{
  return new Symbol(sym.ID, yytext());
}
					case -70:
						break;
					case 83:
						{
  return new Symbol(sym.ID, yytext());
}
					case -71:
						break;
					case 84:
						{
  return new Symbol(sym.ID, yytext());
}
					case -72:
						break;
					case 85:
						{
  return new Symbol(sym.ID, yytext());
}
					case -73:
						break;
					case 87:
						{
  return new Symbol(sym.ID, yytext());
}
					case -74:
						break;
					case 88:
						{
  return new Symbol(sym.ID, yytext());
}
					case -75:
						break;
					case 89:
						{
  return new Symbol(sym.ID, yytext());
}
					case -76:
						break;
					case 90:
						{
  return new Symbol(sym.ID, yytext());
}
					case -77:
						break;
					case 91:
						{
  return new Symbol(sym.ID, yytext());
}
					case -78:
						break;
					case 92:
						{
  return new Symbol(sym.ID, yytext());
}
					case -79:
						break;
					case 93:
						{
  return new Symbol(sym.ID, yytext());
}
					case -80:
						break;
					case 94:
						{
  return new Symbol(sym.ID, yytext());
}
					case -81:
						break;
					case 95:
						{
  return new Symbol(sym.ID, yytext());
}
					case -82:
						break;
					case 96:
						{
  return new Symbol(sym.ID, yytext());
}
					case -83:
						break;
					case 97:
						{
  return new Symbol(sym.ID, yytext());
}
					case -84:
						break;
					case 98:
						{
  return new Symbol(sym.ID, yytext());
}
					case -85:
						break;
					case 99:
						{
  return new Symbol(sym.ID, yytext());
}
					case -86:
						break;
					case 100:
						{
  return new Symbol(sym.ID, yytext());
}
					case -87:
						break;
					case 101:
						{
  return new Symbol(sym.ID, yytext());
}
					case -88:
						break;
					case 102:
						{
  return new Symbol(sym.ID, yytext());
}
					case -89:
						break;
					case 103:
						{
  return new Symbol(sym.ID, yytext());
}
					case -90:
						break;
					case 104:
						{
  return new Symbol(sym.ID, yytext());
}
					case -91:
						break;
					case 105:
						{
  return new Symbol(sym.ID, yytext());
}
					case -92:
						break;
					case 106:
						{
  return new Symbol(sym.ID, yytext());
}
					case -93:
						break;
					case 107:
						{
  return new Symbol(sym.ID, yytext());
}
					case -94:
						break;
					case 108:
						{
  return new Symbol(sym.ID, yytext());
}
					case -95:
						break;
					case 109:
						{
  return new Symbol(sym.ID, yytext());
}
					case -96:
						break;
					case 110:
						{
  int lex = sym.KW;
  switch (yytext()) {
    case "if": lex = sym.IF; break;
    case "else": lex = sym.ELSE; break;
    case "elif": lex = sym.ELIF; break;
    case "for": lex = sym.FOR; break;
    case "while": lex = sym.WHILE; break;
    case "from": lex = sym.FROM; break;
    case "import": lex = sym.IMPORT; break;
    case "def": lex = sym.DEF; break;
    case "try": lex = sym.TRY; break;
    case "except": lex = sym.EXCEPT; break;
    case "continue": lex = sym.CONTINUE; break;
    case "break": lex = sym.BREAK; break;
  }
  return new Symbol(lex, null);
}
					case -97:
						break;
					case 111:
						{
  return new Symbol(sym.ID, yytext());
}
					case -98:
						break;
					case 112:
						{
  return new Symbol(sym.ID, yytext());
}
					case -99:
						break;
					case 113:
						{
  return new Symbol(sym.ID, yytext());
}
					case -100:
						break;
					case 114:
						{
  return new Symbol(sym.ID, yytext());
}
					case -101:
						break;
					case 115:
						{
  return new Symbol(sym.ID, yytext());
}
					case -102:
						break;
					case 116:
						{
  return new Symbol(sym.ID, yytext());
}
					case -103:
						break;
					case 117:
						{
  return new Symbol(sym.ID, yytext());
}
					case -104:
						break;
					case 118:
						{
  return new Symbol(sym.ID, yytext());
}
					case -105:
						break;
					case 119:
						{
  return new Symbol(sym.ID, yytext());
}
					case -106:
						break;
					case 120:
						{
  return new Symbol(sym.ID, yytext());
}
					case -107:
						break;
					case 121:
						{
  return new Symbol(sym.ID, yytext());
}
					case -108:
						break;
					case 122:
						{
  return new Symbol(sym.ID, yytext());
}
					case -109:
						break;
					case 123:
						{
  return new Symbol(sym.ID, yytext());
}
					case -110:
						break;
					case 124:
						{
  return new Symbol(sym.ID, yytext());
}
					case -111:
						break;
					case 125:
						{
  return new Symbol(sym.ID, yytext());
}
					case -112:
						break;
					case 126:
						{
  return new Symbol(sym.ID, yytext());
}
					case -113:
						break;
					case 127:
						{
  return new Symbol(sym.ID, yytext());
}
					case -114:
						break;
					case 128:
						{
  return new Symbol(sym.ID, yytext());
}
					case -115:
						break;
					case 129:
						{
  return new Symbol(sym.ID, yytext());
}
					case -116:
						break;
					case 130:
						{
  return new Symbol(sym.ID, yytext());
}
					case -117:
						break;
					case 131:
						{
  return new Symbol(sym.ID, yytext());
}
					case -118:
						break;
					case 132:
						{
  return new Symbol(sym.ID, yytext());
}
					case -119:
						break;
					case 133:
						{
  return new Symbol(sym.ID, yytext());
}
					case -120:
						break;
					case 134:
						{
  return new Symbol(sym.ID, yytext());
}
					case -121:
						break;
					case 135:
						{
  return new Symbol(sym.ID, yytext());
}
					case -122:
						break;
					case 136:
						{
  return new Symbol(sym.ID, yytext());
}
					case -123:
						break;
					case 137:
						{
  return new Symbol(sym.ID, yytext());
}
					case -124:
						break;
					case 138:
						{
  return new Symbol(sym.ID, yytext());
}
					case -125:
						break;
					case 139:
						{
  return new Symbol(sym.ID, yytext());
}
					case -126:
						break;
					case 140:
						{
  return new Symbol(sym.ID, yytext());
}
					case -127:
						break;
					case 141:
						{
  return new Symbol(sym.ID, yytext());
}
					case -128:
						break;
					case 142:
						{
  return new Symbol(sym.ID, yytext());
}
					case -129:
						break;
					case 143:
						{
  return new Symbol(sym.ID, yytext());
}
					case -130:
						break;
					case 144:
						{
  return new Symbol(sym.ID, yytext());
}
					case -131:
						break;
					case 145:
						{
  return new Symbol(sym.ID, yytext());
}
					case -132:
						break;
					case 146:
						{
  return new Symbol(sym.ID, yytext());
}
					case -133:
						break;
					case 147:
						{
  return new Symbol(sym.ID, yytext());
}
					case -134:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
