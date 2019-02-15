package me.bhop.bcon.node

import java.lang.IllegalArgumentException
import kotlin.reflect.KProperty


abstract class Node(val id: String, val comments: MutableList<String> = mutableListOf(), parent: Node?, val children: MutableList<Node>? = null) {
    var parent: Node? by ParentDelegate(parent)


    fun getAsCategory(): CategoryNode? {
        if (this is CategoryNode)
            return this
        return null
    }

    fun getAsPrimitive(): PrimitiveNode? {
        if (this is PrimitiveNode)
            return this
        return null
    }

    fun getAsArray(): ArrayNode? {
        if (this is ArrayNode)
            return this
        return null
    }


    fun isRoot(): Boolean = parent == null

    fun isCategory(): Boolean = getAsCategory() == null

    fun isPrimitive(): Boolean = getAsPrimitive() == null

    fun isArray(): Boolean = getAsArray() == null


    private class ParentDelegate(var parent: Node? = null) {
        operator fun getValue(ref: Any?, property: KProperty<*>): Node? {
            return parent
        }

        operator fun setValue(ref: Any?, property: KProperty<*>, value: Node?) {
            if (value?.isCategory() != true)
                throw IllegalArgumentException("New parent must be of node type category!")
            if (ref !is Node)
                throw IllegalArgumentException("This delegate is being misused.")
            parent?.children?.remove(ref)
            value.children?.add(ref)
            parent = value
        }
    }
}
