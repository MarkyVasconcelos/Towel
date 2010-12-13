package com.towel.math.exp;

public enum Operator {
	PLUS("+", Operands.DOUBLE, 0) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 + f2;
		}
	},
	MINUS("-", Operands.DOUBLE, 0) {
		public Double resolve(Double f1, Double f2) {
			return f1 - f2;
		}

	},TIMES("*", Operands.DOUBLE, 10) {
		public Double resolve(Double f1, Double f2) {
			return f1 * f2;
		}

	},DIV("/", Operands.DOUBLE, 10) {
		public Double resolve(Double f1, Double f2) {
			return f1 / f2;
		}

	},POW("^", Operands.DOUBLE, 10) {
		public Double resolve(Double f1, Double f2) {
			return Math.pow(f1, f2);
		}

	},MOD("%", Operands.DOUBLE, 10) {
		public Double resolve(Double f1, Double f2) {
			return f1 % f2;
		}

	},COS("cos", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.cos(f1);
		}

	},SIN("sin", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.sin(f1);
		}

	},TAN("tan", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.tan(f1);
		}

	},ACOS("acos", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.acos(f1);
		}

	},ASIN("asin", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.asin(f1);
		}

	},ATAN("atan", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.atan(f1);
		}

	},SQRT("sqrt", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.sqrt(f1);
		}

	},SQR("sqr", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return f1 * f1;
		}

	},LOG("log", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.log(f1);
		}

	},FLOOR("floor", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.floor(f1);
		}

	},CEIL("ceil", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.ceil(f1);
		}

	},ABS("abs", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.abs(f1);
		}

	},NEG("neg", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return -f1;
		}

	},RND("rnd", Operands.SINGLE, 20) {
		public Double resolve(Double f1, Double f2) {
			return Math.random() * f1;
		}
	};
	;

	private String op;
	private Operands type;
	private int priority;

	private Operator(String op, Operands type, int p) {
		this.op = op;
		this.type = type;
		priority = p;
	}

	public abstract Double resolve(Double f1, Double f2);

	public enum Operands {
		SINGLE, DOUBLE;
	}

	public String getOperator() {
		return op;
	}

	public void setOperator(String op) {
		this.op = op;
	}

	public Operands getType() {
		return type;
	}

	public void setType(Operands type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
