package ru.borhammere.dildo.sample.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.borhammere.dildo.sample.domain.NoteRepo
import ru.borhammere.dildo.sample.domain.entities.NoteEntity
import java.util.concurrent.TimeUnit

class NoteRepoWebImpl : NoteRepo {
    private val cache: MutableList<NoteEntity> = mutableListOf()
    private val behaviorSubject = BehaviorSubject.create<List<NoteEntity>>()

    override val notes: Observable<List<NoteEntity>>
        get() = behaviorSubject

    override fun put(note: NoteEntity): Completable =
        Completable.timer(3, TimeUnit.SECONDS).doOnComplete {
            cache.add(note)
            behaviorSubject.onNext(cache)
        }

    override fun clear(): Completable = Completable.timer(3, TimeUnit.SECONDS).doOnComplete {
        cache.clear()
        behaviorSubject.onNext(cache)
    }
}