package org.cognitor.server.openid.service.impl;

import org.cognitor.server.openid.domain.OpenIdAssociation;
import org.cognitor.server.openid.persistence.OpenIdAssociationDao;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openid4java.association.Association;
import org.openid4java.association.AssociationException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class AssociationStoreServiceImplTest {

    @Mock
    private OpenIdAssociationDao daoMock;

    private AssociationStoreServiceImpl store;

    @Before
    public void setUp() {
        store = new AssociationStoreServiceImpl(daoMock);
    }

    @Test
    public void shouldGenerateNewAssociationWhenTypeAndExpiryGiven() throws Exception {
        String sha256 = "HMAC-SHA256";
        int expiry = 60;
        Association association = store.generate(sha256, expiry);

        ArgumentCaptor<OpenIdAssociation> captor = ArgumentCaptor.forClass(OpenIdAssociation.class);
        verify(daoMock, times(1)).save(captor.capture());

        OpenIdAssociation openIdAssociation = captor.getValue();

        assertEquals(sha256, association.getType());
        assertNotNull(association.getHandle());
        assertNotNull(association.getMacKey());
        assertEquals(association.getType(), openIdAssociation.getType());
        assertEquals(association.getExpiry(), openIdAssociation.getExpiry().toDate());
        assertEquals(association.getHandle(), openIdAssociation.getHandle());
        assertArrayEquals(association.getMacKey().getEncoded(), openIdAssociation.getKey());
    }

    @Test(expected = AssociationException.class)
    public void shouldThrowExceptionWhenInvalidTypeGiven() throws Exception {
        store.generate("bla", 0);
    }

    @Test
    public void shouldReturnNullWhenHandleNotPresentInPersistenceGiven() {
        when(daoMock.load(any(String.class))).thenReturn(null);

        assertNull(store.load("unknown"));
    }

    @Test
    public void shouldReturnSsoAssociationWhenHandleExistingInPersistenceForSHA1Given() {
        DateTime expiry = new DateTime(2012, 1, 1, 12, 0 );
        byte[] key = new byte[] { 1, 2, 3 };
        OpenIdAssociation ssoAssociation = new OpenIdAssociation("test-handle", Association.TYPE_HMAC_SHA1,
                expiry, key);
        when(daoMock.load("test-handle")).thenReturn(ssoAssociation);

        Association association = store.load("test-handle");

        assertNotNull(association);
        assertEquals(association.getExpiry(), ssoAssociation.getExpiry().toDate());
        assertEquals(association.getHandle(), ssoAssociation.getHandle());
        assertEquals(association.getType(), ssoAssociation.getType());
        assertArrayEquals(association.getMacKey().getEncoded(), ssoAssociation.getKey());
    }

    @Test
    public void shouldReturnSsoAssociationWhenHandleExistingInPersistenceForSHA256Given() {
        DateTime expiry = new DateTime(2012, 1, 1, 12, 0 );
        byte[] key = new byte[] { 1, 2, 3 };
        OpenIdAssociation ssoAssociation = new OpenIdAssociation("test-handle", Association.TYPE_HMAC_SHA256,
                expiry, key);
        when(daoMock.load("test-handle")).thenReturn(ssoAssociation);

        Association association = store.load("test-handle");

        assertNotNull(association);
        assertEquals(association.getExpiry(), ssoAssociation.getExpiry().toDate());
        assertEquals(association.getHandle(), ssoAssociation.getHandle());
        assertEquals(association.getType(), ssoAssociation.getType());
        assertArrayEquals(association.getMacKey().getEncoded(), ssoAssociation.getKey());
    }

    @Test
    public void shouldReturnNullWhenHandleOfUnknownTypeInPersistenceGiven() {
        when(daoMock.load("test-handle")).thenReturn(new OpenIdAssociation(
               "test-handle", "unknown-type", null, null));

        assertNull(store.load("test-handle"));
    }

    @Test
    public void shouldDeleteAssociationWhenHandleToDeleteGiven() {
        store.remove("test-handle");

        verify(daoMock, times(1)).delete("test-handle");
    }
}