package com.fpoly.sdeliverydriver.ui.delivery

import android.os.Bundle
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.databinding.ActivityDeliveryBinding
import com.fpoly.sdeliverydriver.databinding.ActivityMainBinding

class DeliveryActivity : PolyBaseActivity<ActivityDeliveryBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun getBinding(): ActivityDeliveryBinding {
        return ActivityDeliveryBinding.inflate(layoutInflater)
    }
}