interface GateImplementor {
    fun performOperation(inputs: List<Boolean>): Boolean
}

abstract class LogicGate(protected val implementor: GateImplementor) {
    abstract fun compute(): Boolean
}

class AndGateImplementor : GateImplementor {
    override fun performOperation(inputs: List<Boolean>): Boolean {
        return inputs.all { it }
    }
}

class AndGate2(implementor: GateImplementor) : LogicGate(implementor) {
    private val inputs = mutableListOf<Boolean>()

    fun addInput(value: Boolean) {
        if (inputs.size < 2) {
            inputs.add(value)
        } else {
            throw IllegalStateException("The AND gate does already have 2 inputs")
        }
    }

    override fun compute(): Boolean {
        if (inputs.size != 2) {
            throw IllegalStateException("The AND gate requires exactly 2 inputs")
        }
        return implementor.performOperation(inputs)
    }
}

class AndGate3(implementor: GateImplementor) : LogicGate(implementor) {
    private val inputs = mutableListOf<Boolean>()

    fun addInput(value: Boolean) {
        if (inputs.size < 3) {
            inputs.add(value)
        } else {
            throw IllegalStateException("The AND gate does already have 3 inputs")
        }
    }

    override fun compute(): Boolean {
        if (inputs.size != 3) {
            throw IllegalStateException("The AND gate requires exactly 3 inputs")
        }
        return implementor.performOperation(inputs)
    }
}

class AndGate4(implementor: GateImplementor) : LogicGate(implementor) {
    private val inputs = mutableListOf<Boolean>()

    fun addInput(value: Boolean) {
        if (inputs.size < 4) {
            inputs.add(value)
        } else {
            throw IllegalStateException("The AND gate does already have 4 inputs")
        }
    }

    override fun compute(): Boolean {
        if (inputs.size != 4) {
            throw IllegalStateException("The AND gate requires exactly 4 inputs")
        }
        return implementor.performOperation(inputs)
    }
}

class AndGate8(implementor: GateImplementor) : LogicGate(implementor) {
    private val inputs = mutableListOf<Boolean>()

    fun addInput(value: Boolean) {
        if (inputs.size < 8) {
            inputs.add(value)
        } else {
            throw IllegalStateException("The AND gate does already have 8 inputs")
        }
    }

    override fun compute(): Boolean {
        if (inputs.size != 8) {
            throw IllegalStateException("The AND gate requires exactly 8 inputs")
        }
        return implementor.performOperation(inputs)
    }
}

interface AndGateBuilder<T : LogicGate> {
    fun addInput(value: Boolean): AndGateBuilder<T>
    fun build(): T
}

class AndGate2Builder : AndGateBuilder<AndGate2> {
    private val gate = AndGate2(AndGateImplementor())

    override fun addInput(value: Boolean): AndGateBuilder<AndGate2> {
        gate.addInput(value)
        return this
    }

    override fun build(): AndGate2 {
        return gate
    }
}

class AndGate3Builder : AndGateBuilder<AndGate3> {
    private val gate = AndGate3(AndGateImplementor())

    override fun addInput(value: Boolean): AndGateBuilder<AndGate3> {
        gate.addInput(value)
        return this
    }

    override fun build(): AndGate3 {
        return gate
    }
}

class AndGate4Builder : AndGateBuilder<AndGate4> {
    private val gate = AndGate4(AndGateImplementor())

    override fun addInput(value: Boolean): AndGateBuilder<AndGate4> {
        gate.addInput(value)
        return this
    }

    override fun build(): AndGate4 {
        return gate
    }
}

class AndGate8Builder : AndGateBuilder<AndGate8> {
    private val gate = AndGate8(AndGateImplementor())

    override fun addInput(value: Boolean): AndGateBuilder<AndGate8> {
        gate.addInput(value)
        return this
    }

    override fun build(): AndGate8 {
        return gate
    }
}

enum class State {
    INITIAL,
    PROCESSING,
    COMPLETE
}

class FiniteStateMachine<T : LogicGate> {
    private var state = State.INITIAL
    private var gate: T? = null

    fun setGate(gate: T) {
        when (state) {
            State.INITIAL -> {
                this.gate = gate
                state = State.PROCESSING
            }
            else -> throw IllegalStateException("The gate cant be set in the state: $state")
        }
    }

    fun compute(): Boolean {
        when (state) {
            State.PROCESSING -> {
                state = State.COMPLETE
                return gate?.compute() ?: throw IllegalStateException("The gate wasnt set")
            }
            State.INITIAL -> throw IllegalStateException("The result cant be calculated before setting up the gate")
            State.COMPLETE -> throw IllegalStateException("The result has already been calculated")
        }
    }

    fun reset() {
        state = State.INITIAL
        gate = null
    }
}

fun main() {
    println("\nAND gate with 2 inputs:")
    val fsm2 = FiniteStateMachine<AndGate2>()
    val gate2 = AndGate2Builder()
        .addInput(true)
        .addInput(true)
        .build()
    fsm2.setGate(gate2)
    println("Result: ${fsm2.compute()}")

    fsm2.reset()

    val gate2b = AndGate2Builder()
        .addInput(true)
        .addInput(false)
        .build()
    fsm2.setGate(gate2b)
    println("Result: ${fsm2.compute()}")

    println("\nAND gate with 3 inputs:")
    val fsm3 = FiniteStateMachine<AndGate3>()
    val gate3 = AndGate3Builder()
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    fsm3.setGate(gate3)
    println("Result: ${fsm3.compute()}")

    fsm3.reset()

    val gate3b = AndGate3Builder()
        .addInput(true)
        .addInput(false)
        .addInput(true)
        .build()
    fsm3.setGate(gate3b)
    println("Result: ${fsm3.compute()}")

    println("\nAND gate with 4 inputs:")
    val fsm4 = FiniteStateMachine<AndGate4>()
    val gate4 = AndGate4Builder()
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    fsm4.setGate(gate4)
    println("Result: ${fsm4.compute()}")

    fsm4.reset()

    val gate4b = AndGate4Builder()
        .addInput(true)
        .addInput(true)
        .addInput(false)
        .addInput(true)
        .build()
    fsm4.setGate(gate4b)
    println("Result: ${fsm4.compute()}")

    println("\nAND gate with 8 inputs:")
    val fsm8 = FiniteStateMachine<AndGate8>()

    val gate8 = AndGate8Builder()
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    fsm8.setGate(gate8)
    println("Result: ${fsm8.compute()}")

    fsm8.reset()

    val gate8b = AndGate8Builder()
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(false)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    fsm8.setGate(gate8b)
    println("Result: ${fsm8.compute()}")

}