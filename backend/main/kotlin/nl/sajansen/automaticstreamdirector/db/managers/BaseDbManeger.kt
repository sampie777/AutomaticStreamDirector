package nl.sajansen.automaticstreamdirector.db.managers

import nl.sajansen.automaticstreamdirector.db.DB
import org.hibernate.HibernateException
import java.util.logging.Logger


abstract class BaseDbManager {
    private val logger = Logger.getLogger(BaseDbManager::class.java.name)

    protected fun saveOrUpdateObj(obj: Any): Boolean {
        val session = DB.connection().openSession()

        return try {
            session.beginTransaction()
            session.saveOrUpdate(obj)
            session.transaction.commit()
            true
        } catch (e: HibernateException) {
            logger.severe("Exception occurred when saving object $obj")
            e.printStackTrace()

            if (session.transaction != null) {
                logger.info("Rolling back transaction")
                session.transaction.rollback()
            }
            false
        } finally {
            session.close()
        }
    }

    protected fun list(type: Class<*>): MutableList<Any?>? {
        val session = DB.connection().openSession()

        return try {
            session.beginTransaction()
            val list = session.createQuery("from ${type.simpleName}").list()
            session.transaction.commit()
            list
        } catch (e: HibernateException) {
            logger.severe("Exception occurred when getting list of ${type.name}")
            e.printStackTrace()
            null
        } finally {
            session.close()
        }
    }

    protected fun get(type: Class<*>, id: Long): Any? {
        val session = DB.connection().openSession()

        return try {
            session.get(type, id)
        } catch (e: HibernateException) {
            logger.severe("Exception occurred when getting object ${type.name} with id: $id")
            e.printStackTrace()
            null
        } finally {
            session.close()
        }
    }

    protected fun delete(type: Class<*>, id: Long): Boolean {
        val session = DB.connection().openSession()

        return try {
            session.beginTransaction()
            val result = session.get(type, id)
            session.delete(result)
            session.transaction.commit()
            true
        } catch (e: HibernateException) {
            logger.severe("Exception occurred when deleting object ${type.name} with id: $id")
            e.printStackTrace()

            if (session.transaction != null) {
                logger.info("Rolling back transaction")
                session.transaction.rollback()
            }
            false
        } finally {
            session.close()
        }
    }
}