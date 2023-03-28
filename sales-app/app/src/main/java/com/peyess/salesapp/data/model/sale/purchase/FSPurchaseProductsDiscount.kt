package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.FSDiscountEyeSet

@Keep
@IgnoreExtraProperties
data class FSPurchaseProductsDiscount(
    @Keep
    @JvmField
    @PropertyName("is_overall")
    val isOverall: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("overall")
    val overall: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("has_own_frames")
    val hasOwnFrames: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("left_products")
    val leftProducts: FSDiscountEyeSet = FSDiscountEyeSet(),

    @Keep
    @JvmField
    @PropertyName("right_products")
    val rightProducts: FSDiscountEyeSet = FSDiscountEyeSet(),
)
