package org.n27.stonks.presentation.common.entities

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

internal data class StringResourceWithArgs(
    val resource: StringResource,
    val args: ImmutableList<Arg> = persistentListOf(),
) {

    sealed class Arg {
        data class Text(val value: String): Arg()
        data class Resource(val value: StringResource): Arg()
    }

    @Composable
    fun asString(): String {
        if (args.isEmpty()) return stringResource(resource)

        val formattedArgs = args.map { arg ->
            when (arg) {
                is Arg.Text -> arg.value
                is Arg.Resource -> stringResource(arg.value)
            }
        }.toTypedArray()

        return stringResource(resource, *formattedArgs)
    }
}

