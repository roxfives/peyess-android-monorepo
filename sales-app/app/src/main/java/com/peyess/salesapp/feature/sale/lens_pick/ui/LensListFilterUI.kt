package com.peyess.salesapp.feature.sale.lens_pick.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.data.model.lens.description.LensDescriptionDocument
import com.peyess.salesapp.data.model.lens.groups.LensGroupDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilter
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

private val errorTitleSpacerHeight = 4.dp

@Composable
internal fun FilterDescriptionDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    descriptions: Async<List<LensDescriptionDocument>> = Uninitialized,
    onPickDescription: (descriptionId: String, descriptionName: String) -> Unit = { _, _ -> },
) {
    val descriptionsList: List<LensDescriptionDocument>

    if (descriptions is Success) {
        descriptionsList = descriptions.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + descriptionsList.map { it.name }

            // TODO: use string resource
            title("Selecione a descrição")

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickDescription("", "")
                } else {
                    onPickDescription(descriptionsList[index - 1].id, descriptionsList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
internal fun FilterMaterialDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    materials: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    onPickMaterial: (materialId: String, materialName: String) -> Unit = { _, _ -> },
) {
    val materialsList: List<FilterLensMaterialEntity>

    if (materials is Success) {
        materialsList = materials.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + materialsList.map { it.name }

            // TODO: use string resource
            title("Selecione o material")

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickMaterial("", "")
                } else {
                    onPickMaterial(materialsList[index - 1].id, materialsList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
internal fun FilterSpecialtyDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    specialties: Async<List<FilterLensSpecialtyEntity>> = Uninitialized,
    onPickSpecialty: (groupId: String, groupName: String) -> Unit = { _, _ -> },
) {
    val specialtiesList: List<FilterLensSpecialtyEntity>

    if (specialties is Success) {
        specialtiesList = specialties.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_group_none))
            val options = noneOption + specialtiesList.map { it.name }

            // TODO: use string resource
            title("Selecione a especialidade")

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickSpecialty("", "")
                } else {
                    onPickSpecialty(specialtiesList[index - 1].id, specialtiesList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
internal fun FilterGroupDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    groups: Async<List<LensGroupDocument>> = Uninitialized,
    onPickGroup: (groupId: String, groupName: String) -> Unit = { _, _ -> },
) {
    val groupsList: List<LensGroupDocument>

    if (groups is Success) {
        groupsList = groups.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_group_none))
            val options = noneOption + groupsList.map { it.name }


            // TODO: use string resource
            title("Selecione a categoria")

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickGroup("", "")
                } else {
                    onPickGroup(groupsList[index - 1].id, groupsList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
internal fun FilterLensDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    @StringRes filterTitleRes: Int = R.string.empty_string,
    filterList: List<LensFilter> = emptyList(),

    isEnabled: Boolean = false,
    isLoading: Boolean = false,
    hasFailed: Boolean = false,
    @StringRes errorMessageRes: Int = R.string.error_generic,
    onRetry: () -> Unit = {},

    onPick: (id: String, name: String) -> Unit = { _, _ -> },
) {
    if (hasFailed) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = errorMessageRes))

            Spacer(modifier = Modifier.height(errorTitleSpacerHeight))

            TextButton(onClick = onRetry) {
                Text(text = stringResource(id = R.string.error_generic_retry))
            }
        }
    } else if (isLoading) {
        CircularProgressIndicator()
    } else {
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.lens_filter_action_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_type_none))
            val options = noneOption + filterList.map { it.name }

            title(res = filterTitleRes)

            listItems(list = options) { index, _ ->

                if (index > 0) {
                    onPick(filterList[index - 1].id, filterList[index - 1].name)
                } else {
                    onPick("", "")
                }

                dialogState.hide()
            }
        }
    }
}