package ru.mephi3.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedModel<T> extends CollectionModel<T> {

	public static PagedModel<?> NO_PAGE = new PagedModel<>();

	private PageMetadata metadata;

	/**
	 * Default constructor to allow instantiation by reflection.
	 */
	protected PagedModel() {
		this(new ArrayList<>(), null);
	}

	/**
	 * Creates a new {@link PagedModel} from the given content {@link PageMetadata}
	 * and {@link Link}s.
	 *
	 * @param content  must not be {@literal null}.
	 * @param metadata
	 * @param links
	 */
	public PagedModel(Collection<T> content, @Nullable PageMetadata metadata) {

		super(content);

		this.metadata = metadata;
	}

	/**
	 * Returns the pagination metadata.
	 *
	 * @return the metadata
	 */
	@JsonProperty("page")
	@Nullable
	public PageMetadata getMetadata() {
		return metadata;
	}

	/**
	 * Factory method to easily create a {@link PagedModel} instance from a set of
	 * entities and pagination metadata.
	 *
	 * @param content  must not be {@literal null}.
	 * @param metadata
	 * @return
	 */
	public static <S> PagedModel<S> wrap(Iterable<S> content, PageMetadata metadata) {

		Assert.notNull(content, "Content must not be null!");

		return new PagedModel<>((Collection<S>) content, metadata);
	}

	public static <S> PagedModel<S> wrap(Page<S> page) {

		Assert.notNull(page, "Page must not be null!");

		PageMetadata metadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
		return wrap(page.getContent(), metadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceSupport#toString()
	 */
	@Override
	public String toString() {
		return String.format("PagedResource { content: %s, metadata: %s }", getContent(), metadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.Resources#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(@Nullable Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}

		PagedModel<?> that = (PagedModel<?>) obj;
		boolean metadataEquals = this.metadata == null ? that.metadata == null : this.metadata.equals(that.metadata);

		return metadataEquals && super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.Resources#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = super.hashCode();
		result += this.metadata == null ? 0 : 31 * this.metadata.hashCode();
		return result;
	}

	/**
	 * Value object for pagination metadata.
	 *
	 * @author Oliver Gierke
	 */
	public static class PageMetadata {

		@JsonProperty
		private long size;
		@JsonProperty
		private long totalElements;
		@JsonProperty
		private long totalPages;
		@JsonProperty
		private long number;

		protected PageMetadata() {

		}

		/**
		 * Creates a new {@link PageMetadata} from the given size, number, total
		 * elements and total pages.
		 *
		 * @param size
		 * @param number        zero-indexed, must be less than totalPages
		 * @param totalElements
		 * @param totalPages
		 */
		public PageMetadata(long size, long number, long totalElements, long totalPages) {

			Assert.isTrue(size > -1, "Size must not be negative!");
			Assert.isTrue(number > -1, "Number must not be negative!");
			Assert.isTrue(totalElements > -1, "Total elements must not be negative!");
			Assert.isTrue(totalPages > -1, "Total pages must not be negative!");

			this.size = size;
			this.number = number;
			this.totalElements = totalElements;
			this.totalPages = totalPages;
		}

		/**
		 * Creates a new {@link PageMetadata} from the given size, number and total
		 * elements.
		 *
		 * @param size          the size of the page
		 * @param number        the number of the page
		 * @param totalElements the total number of elements available
		 */
		public PageMetadata(long size, long number, long totalElements) {
			this(size, number, totalElements, size == 0 ? 0 : (long) Math.ceil((double) totalElements / (double) size));
		}

		/**
		 * Returns the requested size of the page.
		 *
		 * @return the size a positive long.
		 */
		public long getSize() {
			return size;
		}

		/**
		 * Returns the total number of elements available.
		 *
		 * @return the totalElements a positive long.
		 */
		public long getTotalElements() {
			return totalElements;
		}

		/**
		 * Returns how many pages are available in total.
		 *
		 * @return the totalPages a positive long.
		 */
		public long getTotalPages() {
			return totalPages;
		}

		/**
		 * Returns the number of the current page.
		 *
		 * @return the number a positive long.
		 */
		public long getNumber() {
			return number;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("Metadata { number: %d, total pages: %d, total elements: %d, size: %d }", number, totalPages, totalElements, size);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(@Nullable Object obj) {

			if (this == obj) {
				return true;
			}

			if (obj == null || !obj.getClass().equals(getClass())) {
				return false;
			}

			PageMetadata that = (PageMetadata) obj;

			return this.number == that.number && this.size == that.size && this.totalElements == that.totalElements && this.totalPages == that.totalPages;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {

			int result = 17;
			result += 31 * (int) (this.number ^ this.number >>> 32);
			result += 31 * (int) (this.size ^ this.size >>> 32);
			result += 31 * (int) (this.totalElements ^ this.totalElements >>> 32);
			result += 31 * (int) (this.totalPages ^ this.totalPages >>> 32);
			return result;
		}
	}
}