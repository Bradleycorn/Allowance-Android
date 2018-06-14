package net.bradball.allowance.models

data class Kid(val id: Int, var firstName: String, var lastName: String,  private var balance: Double = 0.0) {

    fun currentBalance(): Double {
        return balance
    }

    fun credit(amount: Double) {
        balance += amount
    }

    fun debit(amount: Double) {
        if (balance > amount)
            balance -= amount
        else
            balance = 0.0
    }
}