package nl.sajansen.automaticstreamdirector.db.managers

import nl.sajansen.automaticstreamdirector.db.DB


abstract class BaseDbManager {
    protected fun saveOrUpdateObj(obj: Any) {
        val session = DB.connection().openSession()
        session.beginTransaction()
        session.saveOrUpdate(obj)
        session.transaction.commit()
        session.close()
    }

    protected fun list(type: Class<*>): MutableList<Any?>? {
        val session = DB.connection().openSession()
        session.beginTransaction()
        val list = session.createQuery("from ${type.simpleName}").list()
        session.transaction.commit()
        session.close()

        return list
    }

    protected fun get(type: Class<*>, id: Long): Any? {
        val session = DB.connection().openSession()
        val result = session.get(type, id)
        session.close()

        return result
    }
}