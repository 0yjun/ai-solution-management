package phzzk.aisolutionmanagement.api.help.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "help_image",
        indexes = @Index(name = "idx_help_image_order", columnList = "help_id, seq"))
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "description", "seq"})
public class HelpImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_id", nullable = false)
    private Help help;

    @Lob
    @Column(name = "image_blob", nullable = true)
    private byte[] blob;

    @Column(name = "image_description", length = 500)
    private String imageDescription;

    @Column(name = "seq", nullable = false)
    private int seq;

    public HelpImage(byte[] blob, String imageDescription) {
        this.blob = blob;
        this.imageDescription = imageDescription;
    }
}