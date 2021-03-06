package ru.mephi3.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.hateoas.EntityModel;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionModel<T> implements Iterable<T> {

	private final Collection<T> content;

	/**
	 * Creates an empty {@link CollectionModel} instance.
	 */
	protected CollectionModel() {
		this(new ArrayList<>());
	}

	/**
	 * @param content must not be {@literal null}.
	 */
	public CollectionModel(Iterable<T> content) {

		Assert.notNull(content, "Content must not be null!");

		this.content = new ArrayList<>();

		for (T element : content) {
			this.content.add(element);
		}
	}

	/**
	 * Creates a new {@link CollectionModel} instance by wrapping the given domain
	 * class instances into a {@link EntityModel}.
	 *
	 * @param content must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EntityModel<S>, S> CollectionModel<T> wrap(Iterable<S> content) {

		Assert.notNull(content, "Content must not be null!");

		ArrayList<T> resources = new ArrayList<>();

		for (S element : content) {
			resources.add((T) new EntityModel<>(element));
		}

		return new CollectionModel<>(resources);
	}

	/**
	 * Returns the underlying elements.
	 *
	 * @return the content will never be {@literal null}.
	 */
	@JsonProperty("content")
	public Collection<T> getContent() {
		return Collections.unmodifiableCollection(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceSupport#toString()
	 */
	@Override
	public String toString() {
		return String.format("Resources { content: %s, %s }", getContent(), super.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(@Nullable Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}

		CollectionModel<?> that = (CollectionModel<?>) obj;

		boolean contentEqual = this.content == null ? that.content == null : this.content.equals(that.content);
		return contentEqual && super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceSupport#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = super.hashCode();
		result += content == null ? 0 : 17 * content.hashCode();

		return result;
	}
}