package co.omisego.omgshop.testutils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import io.reactivex.Notification;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OmiseGO
 * <p>
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

/**
 * A test {@link Observer} and JUnit rule which guarantees all events are asserted.
 */

public class RecordingObserver<T> implements Observer<T> {
  private final Deque<Notification<T>> events = new ArrayDeque<>();

  private RecordingObserver() {
  }

  @Override
  public void onSubscribe(Disposable disposable) {
  }

  @Override
  public void onNext(T value) {
    events.add(Notification.createOnNext(value));
  }

  @Override
  public void onComplete() {
    events.add(Notification.<T>createOnComplete());
  }

  @Override
  public void onError(Throwable e) {
    events.add(Notification.<T>createOnError(e));
  }

  private Notification<T> takeNotification() {
    Notification<T> notification = events.pollFirst();
    if (notification == null) {
      throw new AssertionError("No event found!");
    }
    return notification;
  }

  public T takeValue() {
    Notification<T> notification = takeNotification();
    assertThat(notification.isOnNext())
            .as("Expected onNext event but was " + notification)
            .isTrue();
    return notification.getValue();
  }

  public Throwable takeError() {
    Notification<T> notification = takeNotification();
    assertThat(notification.isOnError())
            .as("Expected onError event but was " + notification)
            .isTrue();
    return notification.getError();
  }

  public RecordingObserver<T> assertAnyValue() {
    takeValue();
    return this;
  }

  public RecordingObserver<T> assertValue(T value) {
    assertThat(takeValue()).isEqualTo(value);
    return this;
  }

  public void assertComplete() {
    Notification<T> notification = takeNotification();
    assertThat(notification.isOnComplete())
            .as("Expected onCompleted event but was " + notification)
            .isTrue();
    assertNoEvents();
  }

  public void assertError(Throwable throwable) {
    assertThat(takeError()).isEqualTo(throwable);
  }

  public void assertError(Class<? extends Throwable> errorClass) {
    assertError(errorClass, null);
  }

  public void assertError(Class<? extends Throwable> errorClass, String message) {
    Throwable throwable = takeError();
    assertThat(throwable).isInstanceOf(errorClass);
    if (message != null) {
      assertThat(throwable).hasMessage(message);
    }
    assertNoEvents();
  }

  public void assertNoEvents() {
    assertThat(events).as("Unconsumed events found!").isEmpty();
  }

  public static final class Rule implements TestRule {
    final List<RecordingObserver<?>> subscribers = new ArrayList<>();

    public <T> RecordingObserver<T> create() {
      RecordingObserver<T> subscriber = new RecordingObserver<>();
      subscribers.add(subscriber);
      return subscriber;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          base.evaluate();
          for (RecordingObserver<?> subscriber : subscribers) {
            subscriber.assertNoEvents();
          }
        }
      };
    }
  }
}
