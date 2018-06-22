package co.omisego.omgshop.pages.history

import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.pagination.SortDirection


/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
object PaginationConfig {
    val PER_PAGE = 5
    val SORT_BY = Paginable.Transaction.SortableFields.CREATED_AT
    val SORT_DIR = SortDirection.DESCENDING
}
