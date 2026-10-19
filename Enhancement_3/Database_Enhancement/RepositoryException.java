/*
 * Thrown by repository implementations when a storage operation fails.
 *
 * It carries a generic message that can be shown to the user.
 * The original SQLException is kept as the cause for logging/debugging only.
 *
 * It is unchecked so AnimalRepository and AnimalService
 * do not need throws clauses and the existing method signatures stay the same.
 */


public class RepositoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}