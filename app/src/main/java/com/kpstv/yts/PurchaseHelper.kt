package com.kpstv.purchase

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

class PurchaseHelper {

    companion object {
        const val PURCHASE_CLIENT_REQUEST_CODE = 0
        const val ERROR_EXTRA = ""
		
		fun ensurePremium(context: Context) {}
		suspend fun checkIfUserAlreadyExist(z1: String, z2: String) = false
		suspend fun verifyAndActiveUser(z0: String, z1: String, z2: String) : Result<Unit> {
			return Result.failure(Exception())
		}
    }

    data class Builder(private val options: Options) {
        private val purchaseHelper = PurchaseHelper()

        fun setContext(value: Fragment): Builder {
            return this
        }

        fun setContext(value: Activity): Builder {
            return this
        }

        fun build() = purchaseHelper
    }

    fun checkout(onUserExist: () -> Unit, onError: (Exception) -> Unit) {}
}

data class Options(val email: String, val accountId: String)