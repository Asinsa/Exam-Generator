import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';

@customElement('quiz-generation-view')
export class QuizGenerationView extends LitElement {
  static get styles() {
    return css`
      :host {
          display: block;
          height: 100%;
      }
      `;
  }

  render() {
    return html`
<vaadin-horizontal-layout theme="spacing" style="align-self: stretch; justify-content: center;">
 <vaadin-vertical-layout theme="spacing" id="allQuestions"></vaadin-vertical-layout>
 <vaadin-vertical-layout theme="spacing" id="chosenQuestions"></vaadin-vertical-layout>
</vaadin-horizontal-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
