package nl.sajansen.automaticstreamdirector.db.managers


@Suppress("UNCHECKED_CAST")
open class CommonDbManager<T>(private val clazz: Class<T>) : BaseDbManager() {
    open fun saveOrUpdate(obj: T): Boolean {
        return super.saveOrUpdateObj(obj as Any)
    }

    open fun list(): MutableList<T?>? {
        return super.list(clazz) as MutableList<T?>?
    }

    open fun get(id: Long): T? {
        return super.get(clazz, id) as T?
    }

    open fun delete(id: Long): Boolean {
        return super.delete(clazz, id)
    }
}