/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.audio;

import gruppe5.common.data.Entity;

/**
 *
 * @author Christian
 */
public interface AudioSPI {
    void playAudio(String soundURL, Entity entity);
}
